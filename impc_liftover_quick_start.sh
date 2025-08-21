#!/bin/bash

# IMPC Liftover Service - Dependency Setup Script
# This script sets up the liftover service as a dependency for the IMPC production tracker
# The liftover service provides coordinate conversion functionality required by the main application

set -e  # Exit on any error

echo "🔧 IMPC Liftover Service - Dependency Setup"
echo "=========================================="
echo "Setting up liftover service as a dependency for IMPC production tracker"
echo ""

# Configuration
IMAGE_NAME="mousephenotype/impc-liftover-service:latest"
CONTAINER_NAME="impc-liftover-dependency"
HOST_PORT="5001"
CONTAINER_PORT="5000"

# Function to check if Docker is running
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        echo "❌ Docker is not running. Please start Docker and try again."
        exit 1
    fi
    echo "✅ Docker is running"
}

# Function to stop and remove existing container
cleanup_existing() {
    if docker ps -a --format "table {{.Names}}" | grep -q "^${CONTAINER_NAME}$"; then
        echo "🔄 Stopping and removing existing dependency container..."
        docker stop ${CONTAINER_NAME} > /dev/null 2>&1 || true
        docker rm ${CONTAINER_NAME} > /dev/null 2>&1 || true
        echo "✅ Existing dependency container cleaned up"
    fi
}

# Function to pull the image
pull_image() {
    echo "📥 Pulling liftover dependency image..."
    docker pull ${IMAGE_NAME}
    echo "✅ Dependency image pulled successfully"
}

# Function to run the container
run_container() {
    echo "🏃 Starting liftover dependency service..."
    docker run -d -p ${HOST_PORT}:${CONTAINER_PORT} --name ${CONTAINER_NAME} ${IMAGE_NAME}
    echo "✅ Dependency service started successfully"
}

# Function to wait for service to be ready
wait_for_service() {
    echo "⏳ Waiting for dependency service to be ready..."
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s "http://localhost:${HOST_PORT}/liftover?chrom=chr9&start=54548017&end=54548066" > /dev/null 2>&1; then
            echo "✅ Dependency service is ready!"
            return 0
        fi
        echo "   Attempt $attempt/$max_attempts - waiting..."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo "❌ Dependency service failed to start within expected time"
    return 1
}

# Function to test the service
test_service() {
    echo "🧪 Testing dependency service..."
    echo "   Making test request..."
    
    local response=$(curl -s "http://localhost:${HOST_PORT}/liftover?chrom=chr9&start=54548017&end=54548066")
    
    if echo "$response" | grep -q "mappings"; then
        echo "✅ Dependency service test successful!"
        echo ""
        echo "📊 Test Results:"
        echo "$response" | python3 -m json.tool 2>/dev/null || echo "$response"
    else
        echo "❌ Dependency service test failed"
        echo "Response: $response"
        return 1
    fi
}

# Function to show dependency information
show_dependency_info() {
    echo ""
    echo "📋 Dependency Information:"
    echo "=========================="
    echo "Service: IMPC Liftover Service (Coordinate Conversion)"
    echo "Purpose: Provides coordinate conversion functionality for the main IMPC production tracker"
    echo "Status: Running as dependency service"
    echo "Endpoint: http://localhost:${HOST_PORT}/liftover"
    echo ""
    echo "🔗 Integration:"
    echo "  - The main IMPC production tracker will connect to this service"
    echo "  - Used for converting genomic coordinates between different assemblies"
    echo "  - Required for accurate data processing in the production tracker"
    echo ""
    echo "📝 Dependency Management:"
    echo "  docker stop ${CONTAINER_NAME}    # Stop the dependency service"
    echo "  docker start ${CONTAINER_NAME}   # Start the dependency service"
    echo "  docker logs ${CONTAINER_NAME}    # View dependency logs"
    echo "  docker rm ${CONTAINER_NAME}      # Remove the dependency container"
    echo ""
    echo "⚠️  Note: This service must remain running for the main IMPC production tracker to function properly"
}

# Main execution
main() {
    check_docker
    cleanup_existing
    pull_image
    run_container
    wait_for_service
    test_service
    show_dependency_info
    
    echo ""
    echo "✅ IMPC Liftover dependency service is ready!"
    echo "   The main IMPC production tracker can now use this service for coordinate conversion"
    echo "   Keep this service running while using the production tracker application"
}

# Run main function
main "$@"
